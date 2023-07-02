const { Configuration, OpenAIApi } = require("openai");
const Busboy = require("busboy");

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

const parseReceipt = async (event) => {
  try {
    let buffer = null;
    const busboy = new Busboy({ headers: event.headers });
    await new Promise((resolve, reject) => {
      busboy.on("file", (fieldname, file, filename, encoding, mimetype) => {
        if (fieldname === "receipt") {
          file.on("data", (data) => {
            if (!buffer) {
              buffer = data;
            } else {
              buffer = Buffer.concat([buffer, data]);
            }
          });
        }
      });

      busboy.on("finish", () => {
        if (buffer) {
          resolve();
        } else {
          reject(new Error("No file found in the request"));
        }
      });

      busboy.on("error", reject);
      busboy.end(event.body);
    });
    
    const worker = await createWorker();
    await worker.loadLanguage("eng");
    await worker.initialize("eng");
    await worker.setParameters({
      tessedit_char_whitelist:
        "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
    });
    const { data: { text } } = await worker.recognize(buffer);
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

    console.log(text);
    console.log(response.data.choices[0].text);

    const responseBody = {
      success: true,
      raw: text,
      gpt: dummy_json,
    };

    return {
      statusCode: 200,
      body: JSON.stringify(responseBody),
    };
  } catch (err) {
    console.error(err);
    return {
      statusCode: 500,
      body: JSON.stringify({
        error: err.message,
        success: false,
        message: "Cannot parse receipt",
      }),
    };
  }
};

module.exports.handler = parseReceipt;
