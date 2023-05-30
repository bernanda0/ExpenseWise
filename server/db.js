// for .env file
require("dotenv").config();

const { Client } = require("pg");

// connect to database using pg
const { PGHOST, PGDATABASE, PGUSER, PGPASSWORD, ENDPOINT_ID } = process.env;
const URL = `postgres://${PGUSER}:${PGPASSWORD}@${PGHOST}/${PGDATABASE}?options=project%3D${ENDPOINT_ID}`;

const db = new Client({
  connectionString: URL,
  ssl: {
    rejectUnauthorized: false,
  },
});

db.connect((err) => {
  if (err) {
    console.log(err);
    return;
  }
  console.log("Connected to Neon DB!");
});

module.exports = db;