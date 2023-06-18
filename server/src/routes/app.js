const express = require("express");
const router = express.Router();
const pool = require("../db/db.js");
express().use(express.json());
express().use(express.urlencoded({ extended: true }));

const { SERVER_BASE_URL } = process.env;
const baseUrl = `${SERVER_BASE_URL}/app`;

// function to handle session, if user not loggin redierct to sessionError
const sessionChecker = (req, res, next) => {
  console.log(req.session.passport);
  if (req.session.passport) {
    next();
  } else {
    res.redirect(baseUrl + "/sessionError");
  }
};

router.get("/sessionError", (req, res) => {
  res.status(401).json({
    success: false,
    message: "User not logged in or session expired",
  });
});

// GET ENDPOINT
// User
router.get("/user", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const query = "SELECT * FROM get_user($1);";
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
router.get("/wallet", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const query =
      "SELECT * FROM wallet WHERE wid = (SELECT wid FROM users WHERE uid = $1);";
    const wallet = await pool.query(query, [uid]);
    res.status(200).json({
      success: true,
      wallet: wallet.rows[0],
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
router.get("/transaction/getAll", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    // sort by t_time
    const query = "SELECT * FROM get_transactions($1) ORDER BY t_time DESC;";
    const transactions = await pool.query(query, [uid]);
    // if the returned value is only one change it to array
    if (transactions.rows.length === 1) {
      transactions.rows = [transactions.rows];
    }
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

router.get("/transaction/getOne", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { tid } = req.body;
    const query = "SELECT * FROM get_transactions($1) WHERE tid = $2;";
    const transaction = await pool.query(query, [uid, tid]);
    res.status(200).json({
      success: true,
      transaction: transaction.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot get transaction",
    });
  }
});

// Get the json summary for downloading
router.get("/summary", sessionChecker, async (req, res) => {
  try {
    let summary = {
      user: {
        username: "",
        email: "",
        occupation: "",
      },
      time_range: {
        start: "",
        end: "",
      },
      wallet: {
        balance: 0,
        total_income: 0,
        total_expense: 0,
      },
      transactions: [],
    };
    const uid = req.session.passport.user;
    const user = await pool.query("SELECT * FROM users WHERE uid = $1;", [uid]);
    const time_range = await pool.query("SELECT * FROM get_time_range($1);", [
      uid,
    ]);
    const wallet = await pool.query(
      "SELECT * FROM wallet WHERE wid = (SELECT wid FROM users WHERE uid = $1);",
      [uid]
    );
    const transactions_query = await pool.query(
      "SELECT * FROM get_transactions($1);",
      [uid]
    );

    // user summary
    summary.user.username = user.rows[0].username;
    summary.user.email = user.rows[0].email;
    summary.user.occupation = user.rows[0].occupation;

    // time range summary
    summary.time_range.start = time_range.rows[0].oldest_time;
    summary.time_range.end = time_range.rows[0].newest_time;

    // wallet summary
    summary.wallet.balance = wallet.rows[0].balance;
    summary.wallet.total_income = wallet.rows[0].total_income;
    summary.wallet.total_expense = wallet.rows[0].total_expense;

    // transactions summary
    transactions_query.rows.forEach((transaction) => {
      let temp = {
        description: transaction.t_description,
        amount: transaction.t_amount,
        transaction_type: transaction.tid.startsWith("i")
          ? "income"
          : "expense",
        category: transaction.t_category,
        time: transaction.t_time,
      };
      summary.transactions.push(temp);
    });

    console.log(summary);
    res.status(200).json({
      success: true,
      summary: summary,
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot get summary",
    });
  }
});

// Get the all category of income and its percentage
router.get("/income/category", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const query = "SELECT * FROM icat_percentage($1);";
    const income_category = await pool.query(query, [uid]);
    res.status(200).json({
      success: true,
      income_category: income_category.rows,
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot get income category",
    });
  }
});

// Get the all category of expense and its percentage
router.get("/expense/category", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;

    const query = "SELECT * FROM ecat_percentage($1);";
    const expense_category = await pool.query(query, [uid]);
    res.status(200).json({
      success: true,
      expense_category: expense_category.rows,
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot get expense category",
    });
  }
});

// POST ENDPOINT
// Income Insert
router.post("/income/insert", sessionChecker, async (req, res) => {
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
router.post("/expense/insert", sessionChecker, async (req, res) => {
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
router.put("/income/update", sessionChecker, async (req, res) => {
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
router.put("/expense/update", sessionChecker, async (req, res) => {
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
router.delete("/income/delete", sessionChecker, async (req, res) => {
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
router.delete("/expense/delete", sessionChecker, async (req, res) => {
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

// Goal get
router.get("/goal", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const query = "SELECT * FROM user_goal WHERE uid = $1;";
    const goal = await pool.query(query, [uid]);
    // if the returned value is only one change it to array
    if (goal.rows.length === 1) {
      goal.rows = [goal.rows];
    }
    res.status(200).json({
      success: true,
      goal: goal.rows[0],
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot get goal",
    });
  }
});

// Goal insert
router.post("/goal/insert", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { goal_expense, end_period } = req.body;
    const query = `INSERT INTO user_goal (uid, goal_expense, end_period) VALUES
        ($1, $2, $3) RETURNING *;`;
    const inserted_goal = await pool.query(query, [
      uid,
      goal_expense,
      end_period,
    ]);
    res.status(200).json({
      success: true,
      goal: null,
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot insert goal",
    });
  }
});

// Goal update
router.put("/goal/update", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { gid, goal_expense, end_period } = req.body;
    const query = `UPDATE user_goal SET goal_expense = $1, end_period = $2
        WHERE ugid = $3 AND uid = $4 RETURNING *;`;
    const updated_goal = await pool.query(query, [
      goal_expense,
      end_period,
      gid,
      uid,
    ]);
    res.status(200).json({
      success: true,
      goal: null,
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot update goal",
    });
  }
});

// Goal delete
router.delete("/goal/delete", sessionChecker, async (req, res) => {
  try {
    const uid = req.session.passport.user;
    const { gid } = req.body;
    const query = `DELETE FROM user_goal WHERE ugid = $1 AND uid = $2 RETURNING *;`;
    const deleted_goal = await pool.query(query, [gid, uid]);
    res.status(200).json({
      success: true,
      goal: null,
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).json({
      success: false,
      message: "Cannot delete goal",
    });
  }
});

module.exports = router;
