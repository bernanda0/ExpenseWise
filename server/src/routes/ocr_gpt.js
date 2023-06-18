const { Configuration, OpenAIApi } = require("openai");
const express = require("express");
const multer = require("multer");
const { createWorker } = require("tesseract.js");
const router = express.Router();
express().use(express.urlencoded({ extended: true }));

const storage = multer.memoryStorage();
const upload = multer({ storage: storage });

const listParams = {
  key: "",
  model: "text-davinci-003",
  prompt: "extract the date, amount, and one sentence description in JSON format:",
  temperature: 0.7,
  max_tokens: 256,
  top_p: 1,
  frequency_penalty: 0,
  presence_penalty: 0,
};

const configuration = new Configuration({
  organization: process.env.OPENAI_ORG_ID,
  apiKey: process.env.OPENAI_API_KEY,
});

const openai = new OpenAIApi(configuration);

router.post("/parse-receipt", upload.single("receipt"), async (req, res) => {
  try {
    const imgBuffer = req.file.buffer;
    const worker = await createWorker();
    await worker.loadLanguage("eng");
    await worker.initialize("eng");
    await worker.setParameters({
      tessedit_char_whitelist:
        "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
    });
    const {
      data: { text },
    } = await worker.recognize(imgBuffer);
    await worker.terminate();

    const response = await openai.createCompletion({
      model: listParams.model,
      prompt: listParams.prompt + "\n" + text + "\n",
      temperature: listParams.temperature,
      max_tokens: listParams.max_tokens,
      top_p: listParams.top_p,
      frequency_penalty: listParams.frequency_penalty,
      presence_penalty: listParams.presence_penalty,
    });

    // convert to JSON
    const gpt_response = response.data.choices[0].text;
    const gpt_json = JSON.parse(gpt_response);
    console.log(text);
    console.log(response.data.choices[0].text);

    res.status(200).json({
      success: true,
      raw: text,
      gpt: gpt_json,
    });

  } catch (err) {
    console.error(err);
    res.status(500).json({
      success: false,
      message: "Cannot parse receipt",
    });
  }
});

module.exports = router;