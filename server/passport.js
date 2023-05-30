//  Using Two Strategies : Google and local
const { Strategy: GoogleStrategy } =
  require("passport-google-oauth20").Strategy;
const { Strategy: LocalStrategy } = require("passport-local").Strategy;

// Importing modules
const passport = require("passport");
const bcrypt = require("bcrypt");
const pool = require("./db");

// Local Strategy
passport.use(
  new LocalStrategy(
    {
      usernameField: "email",
      passwordField: "password",
    },
    async (email, password, done) => {
      try {
        const user = await pool.query("SELECT * FROM users WHERE email = $1", [
          email,
        ]);
        if (user.rows.length > 0) {
          const isMatch = await bcrypt.compare(password, user.rows[0].password);
          if (isMatch) {
            console.log("User successfully logged in!");
            done(null, user.rows[0]);
          } else {
            console.log("Incorrect password");
            done(null, false, { message: "Incorrect password" });
          }
        } else {
          console.log("User not registered");
          done(null, false, { message: "User not registered" });
        }
      } catch (error) {
        console.log(error);
        done(error);
      }
    }
  )
);

passport.use(
  new GoogleStrategy(
    {
      clientID: process.env.GOOGLE_CLIENT_ID,
      clientSecret: process.env.GOOGLE_CLIENT_SECRET,
      callbackURL: "/auth/google/callback",
      scope: ["profile", "email"],
    },
    async (_, __, ____, profile, done) => {
      const account = profile._json;
      let logged_user = {};
      try {
        const user = await pool.query("SELECT * FROM users WHERE email = $1", [
          account.email,
        ]);
        if (user.rows.length > 0) {
          console.log("User already registered, continuing to login");
          logged_user = {
            username: user.rows[0].username,
            email: user.rows[0].email,
            img: user.rows[0].img,
          };
        } else {
          await pool.query(
            "INSERT INTO users (username, email, img) VALUES ($1, $2, $3) RETURNING *",
            [account.name, account.email, account.picture]
          );
          console.log("User successfully registered in database");
          logged_user = {
            username: account.name,
            email: account.email,
            img: account.picture,
          };
        }
        done(null, logged_user);
      } catch (err) {
        console.log(err);
        done(err);
      }
    }
  )
);

passport.serializeUser((user, done) => {
  done(null, user);
});

passport.deserializeUser((user, done) => {
  done(null, user);
});
