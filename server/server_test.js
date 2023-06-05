// for .env file
require("dotenv").config();

// Importing modules
const express = require("express");
const cors = require("cors");
const passport = require("passport");
const session = require("express-session");
const connect_pg = require("connect-pg-simple");
const serverless = require("serverless-http");
const auth_routes = require("./src/routes/auth.js");
const app_routes = require("./src/routes/app.js");
const ovo_routes = require("./src/routes/ovo.js");
const gpt_routes = require("./src/routes/ocr_gpt.js");
require("./src/passport.js");

// pg pool
const pool = require("./src/db.js");

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

const router = express.Router();
router.get("/", (req, res) => {
  res.send("WELCOME TO EW API💸");
});

const { SERVER_BASE_URL } = process.env;
// routing
app.use(`${SERVER_BASE_URL}`, router) // path must route to lambda
app.use(`${SERVER_BASE_URL}/auth`, auth_routes);
app.use(`${SERVER_BASE_URL}/app`, app_routes);
app.use(`${SERVER_BASE_URL}/ovo`, ovo_routes);
app.use(`${SERVER_BASE_URL}/gpt`, gpt_routes);

app.listen(process.env.PORT || 8463, () => {
  console.log(`BESE SERVER URL ${SERVER_BASE_URL}`)
  console.log(`App Started on PORT ${process.env.PORT || 8463}`);
});
