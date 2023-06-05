const express = require("express");
const router = express.Router();
const passport = require("passport");
const pool = require("../db.js");
const bcrypt = require("bcryptjs");

const { SERVER_BASE_URL } = process.env;
const baseUrl = `${SERVER_BASE_URL}/auth`;

router.get("/", (req, res) => {
  if (req.user) {
    res.send(`Welcome ${req.user.username}`);
  } else {
    res.send("Need to login first");
  }
});

router.get(
  "/google/callback",
  passport.authenticate("google", {
    session: true,
  }),
  (req, res) => {
    if (req.user) {
      res.redirect(baseUrl + '/login/success');
    } else {
      res.redirect(baseUrl + '/login/fail');
    }
  }
);

router.get(
  "/google",
  passport.authenticate("google", { scope: ["profile", "email"] })
);

// verify token from google login in android app
router.post(
  "/google/verify_token",
  passport.authenticate("google-id-token"),
  (req, res) => {
    if (req.user) {
      res.redirect(baseUrl + '/login/success');
    } else {
      res.redirect(baseUrl + '/login/fail');
    }
  }
);

router.get("/login/success", (req, res) => {
  if (req.user) {
    res.status(200).json({
      success: true,
      message: `Welcome ${req.user.username}!`,
      uid: req.user.uid,
    });
  } else {
    res.status(401).json({
      success: false,
      message: "User has not been authenticated",
    });
  }
});

router.get("/login/failed", (req, res) => {
  const message = req.query.message || "Login failed";
  res.status(400).json({
    success: false,
    message: message,
  });
});

router.get("/logout", (req, res) => {
  req.logout((err) => {
    if (err) {
      res.status(500).send(err);
    } else {
      req.session.destroy();
      console.log("User logged out");
      res.status(200).json({
        success: true,
        message: "User logged out",
      });
    }
  });
});

router.post("/register", async (req, res) => {
  const { username, email, password } = req.body;
  try {
    const user = await pool.query("SELECT * FROM users WHERE email = $1", [
      email,
    ]);
    if (user.rows.length > 0) {
      console.log("User already registered");
      res.status(400).json({
        success: false,
        message: "User already registered",
      });
    } else {
      // Create new wallet for new user
      const newWallet = await pool.query(
        "INSERT INTO wallet (balance) VALUES (0) RETURNING *"
      );
      const walletId = newWallet.rows[0].wid;
      // Create new user
      const salt = await bcrypt.genSalt(10);
      const bcryptPassword = await bcrypt.hash(password, salt);
      const newUser = await pool.query(
        "INSERT INTO users (username, email, password, wid) VALUES ($1, $2, $3, $4) RETURNING *",
        [username, email, bcryptPassword, walletId]
      );
      console.log("User successfully registered");
      req.login(newUser.rows[0].uid, (err) => {
        if (err) {
          console.log(err);
          return next(err);
        }
        console.log("Automatic login successful");
        return res.redirect(baseUrl + "/login/success");
      });
    }
  } catch (error) {
    console.log(error);
    res.status(400).send(error.message);
  }
});

router.post(
  "/login/local",
  (req, res, next) => {
    passport.authenticate("local", (err, user, info) => {
      if (err) {
        return next(err);
      }
      if (!user) {
        return res.redirect(baseUrl + "/login/failed?message=" + encodeURIComponent(info.message));
      }
      req.logIn(user, (err) => {
        if (err) {
          return next(err);
        }
        return res.redirect(baseUrl + "/login/success");
      });
    })(req, res, next);
  }
);

module.exports = router;
