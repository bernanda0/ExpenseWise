const express = require("express");
const router = express.Router();
const pool = require("../db.js");
express().use(express.json());
express().use(express.urlencoded({ extended: true }));

// GET ENDPOINT
// User
router.get("/user", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const query = "SELECT * FROM users WHERE uid = $1;";
    const user = await pool.query(query, [uid]);
    res.status(200).json({
      success: true,
      user: user.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot get user info",
    });
  }
});

// Balance
router.get("/wallet", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const query =
      "SELECT * FROM wallet WHERE wid = (SELECT wid FROM users WHERE uid = $1);";
    const balance = await pool.query(query, [uid]);
    res.status(200).json({
      success: true,
      balance: balance.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot get user wallet",
    });
  }
});

// All the transaction
router.get("/transactions", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const query = "SELECT * FROM get_transactions($1);";
    const transactions = await pool.query(query, [uid]);
    res.status(200).json({
      success: true,
      transactions: transactions.rows,
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot get user transactions",
    });
  }
});

// POST ENDPOINT
// Income Insert
router.post("/income/insert", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { icid, amount, time, description } = req.body;
    const query = `INSERT INTO income (uid, icid, amount, time, description) VALUES
        ($1, $2, $3, $4, $5) RETURNING *;`;
    const inserted_income = await pool.query(query, [
      uid,
      icid,
      amount,
      time,
      description,
    ]);
    res.status(200).json({
      success: true,
      income: inserted_income.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot insert income",
    });
  }
});

// Expense Insert
router.post("/expense/insert", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { ecid, amount, time, description } = req.body;
    const query = `INSERT INTO expense (uid, ecid, amount, time, description) VALUES 
        ($1, $2, $3, $4, $5) RETURNING *;`;
    const inserted_expense = await pool.query(query, [
      uid,
      ecid,
      amount,
      time,
      description,
    ]);
    res.status(200).json({
      success: true,
      expense: inserted_expense.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot insert expense",
    });
  }
});

//PUT ENDPOINT
// Income Update
router.put("/income/update", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { iid, icid, amount, time, description } = req.body;
    const query = `UPDATE income SET icid = $1, amount = $2, time = $3, description = $4
        WHERE iid = $5 AND uid = $6 RETURNING *;`;
    const updated_income = await pool.query(query, [
      icid,
      amount,
      time,
      description,
      iid,
      uid,
    ]);
    res.status(200).json({
      success: true,
      income: updated_income.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot update income",
    });
  }
});

// expense Update
router.put("/expense/update", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { eid, ecid, amount, time, description } = req.body;
    const query = `UPDATE expense SET ecid = $1, amount = $2, time = $3, description = $4
        WHERE eid = $5 AND uid = $6 RETURNING *;`;
    const updated_expense = await pool.query(query, [
      ecid,
      amount,
      time,
      description,
      eid,
      uid,
    ]);
    res.status(200).json({
      success: true,
      expense: updated_expense.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot update expense",
    });
  }
});

// DELETE ENDPOINT
// Income Delete
router.delete("/income/delete", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { iid } = req.body;
    const query = `DELETE FROM income WHERE iid = $1 AND uid = $2 RETURNING *;`;
    const deleted_income = await pool.query(query, [iid, uid]);
    res.status(200).json({
      success: true,
      income: deleted_income.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot delete income",
    });
  }
});

// Expense Delete
router.delete("/expense/delete", async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { eid } = req.body;
    const query = `DELETE FROM expense WHERE eid = $1 AND uid = $2 RETURNING *;`;
    const deleted_expense = await pool.query(query, [eid, uid]);
    res.status(200).json({
      success: true,
      expense: deleted_expense.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot delete expense",
    });
  }
});

module.exports = router;
