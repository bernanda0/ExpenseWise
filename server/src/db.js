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
  idleTimeoutMillis: 0,
  connectionTimeoutMillis: 0,
});

db.connect((err) => {
  try {
    console.log("Connected to Neon DB!");
  } catch (error) {
    console.log(err);
    return;
  } 
});

module.exports = db;