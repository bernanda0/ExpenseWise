// for .env file
require("dotenv").config();

// Importing modules
const express = require("express");
const cors = require("cors");
const passport = require("passport");
const session = require("express-session");
const bcrypt = require("bcrypt");
const auth_routes = require("./routes/auth.js");
require("./passport.js");
require("./db.js");

// Instantiate the express app and middleware
const app = express();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(
  session({
    secret: process.env.COOKIE_SECRET,
    cookie: {
      maxAge: 1000 * 60 * 60 * 24 * 7, // 1 week
      secure: "auto",
      sameSite: "lax",
    },
    resave: true,
    saveUninitialized: true,
  })
);
app.use(
  cors({
    origin: process.env.CLIENT_URL,
    credentials: true,
    methods: ["GET", "POST", "PUT", "DELETE"],
  })
);
app.use(passport.initialize());
app.use(passport.session());

// routing
app.use("/auth", auth_routes);

// start the app
app.listen(process.env.PORT || 8463, () => {
  console.log(`App Started on PORT ${process.env.PORT || 8463}`);
});
