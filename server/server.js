// for .env file
require("dotenv").config();

// Importing modules
const express = require("express");
const cors = require("cors");
const passport = require("passport");
const session = require("express-session");
const connect_pg = require("connect-pg-simple");
const auth_routes = require("./routes/auth.js");
const app_routes = require("./routes/app.js");
const ovo_routes = require("./routes/ovo.js");
const gpt_routes = require("./routes/ocr_gpt.js");
require("./passport.js");

// pg pool
const pool = require("./db.js");

// Instantiate the express app and middleware
const app = express();
const PostgresqlStore = connect_pg(session);
const sessionStore = new PostgresqlStore({
  pool: pool,
  tableName: "session",
});
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
    store: sessionStore,
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
app.use("/app", app_routes);
app.use("/ovo", ovo_routes);
app.use("/gpt", gpt_routes);

// start the app
app.listen(process.env.PORT || 8463, () => {
  console.log(`App Started on PORT ${process.env.PORT || 8463}`);
});
