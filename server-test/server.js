const express = require('express');
const bodyParser = require('body-parser');
const app = express();

app.use(bodyParser.json());

app.post('/api/todos', (req, res) => {
  const newTodo = req.body;
  console.log(newTodo);
  res.status(201).json(newTodo);
});

const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
