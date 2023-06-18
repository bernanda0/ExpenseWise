const express = require("express");
const router = express.Router();
express().use(express.json());
express().use(express.urlencoded({ extended: true }));
const pool = require("../db/db.js");

const OVOID = require("ovoid");
let ovoid = new OVOID();

router.get("/login/:phone", async (req, res) => {
  try {
    let refId = await ovoid.login2FA(req.params.phone);
    req.session.refId = refId;
    req.session.phone = req.params.phone;
    res.status(200).json({
      success: true,
      refId: refId,
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({
      success: false,
      message: "Cannot login to ovo",
    });
  }
});

router.get("/login/otp/:otp", async (req, res) => {
  try {
    const { otp_refId, deviceId } = req.session.refId;
    const phone = req.session.phone;
    let access_token = await ovoid.login2FAVerify(
      otp_refId,
      req.params.otp,
      phone,
      deviceId
    );
    req.session.otp_token = access_token.otp_token;
    res.status(200).json({
      success: true,
      result: access_token,
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({
      success: false,
      message: "Cannot login to ovo",
    });
  }
});

router.get("/login/pin/:pin", async (req, res) => {
  try {
    const otp_token = req.session.otp_token;
    const { otp_refId, deviceId } = req.session.refId;
    const phone = req.session.phone;

    let authToken = await ovoid.loginSecurityCode(
      req.params.pin,
      otp_token,
      phone,
      otp_refId,
      deviceId
    );

    req.session.refresh_token = authToken.refresh_token;
    console.log(authToken.refresh_token);
    const query = "INSERT INTO e_wallet_token (type, token) VALUES ($1, $2);";
    await pool.query(query, ['OVO', authToken.refresh_token]);

    res.status(200).json({
      success: true,
      result: authToken,
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({
      success: false,
      message: "Cannot login to ovo",
    });
  }
});

router.get("/balance", async (req, res) => {
  try {
    let refresh_token = req.session.refresh_token;
    ovoid = new OVOID(refresh_token);
    let balance = await ovoid.getBalance("cash");
    res.status(200).json({
      success: true,
      balance: balance,
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({
      success: false,
      message: "Cannot get balance",
    });
  }
});

router.get("/profile", async (req, res) => {
  try {
    let refresh_token = req.session.refresh_token;
    ovoid = new OVOID(refresh_token);
    let profile = await ovoid.getProfile();
    res.status(200).json({
      success: true,
      profile: profile,
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({
      success: false,
      message: "Cannot get profile",
    });
  }
});

router.get("/notifications", async (req, res) => {
  try {
    let refresh_token = req.session.refresh_token;
    ovoid = new OVOID(refresh_token);
    let notifications = await ovoid.getAllNotification();
    // filter the notification so that it only contain
    let filtered_notif = [{}];
    notifications.notifications.forEach((n) => {
      if (n.messageType == "RECEIPT" || n.messageType == "TOPUP_INFO") {
        filtered_notif.push({
          messageType: n.messageType,
          message: n.message,
        });
      }
    });
    console.log(filtered_notif);
    res.status(200).json({
      success: true,
      notifications: notifications,
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({
      success: false,
      message: "Cannot get notifications",
    });
  }
});

module.exports = router;
