const express = require('express'); // 导入 Express 框架
const bodyParser = require('body-parser'); // 导入 body-parser 中间件
const app = express(); // 创建 Express 应用
const PORT = 3000; // 定义服务器端口

app.use(bodyParser.json()); // 使用 body-parser 中间件解析 JSON 请求体

const users = [
  { id: 1, username: 'postgres', password: 'huarui66', role: 'admin' }, // 用户1
  { id: 2, username: 'user2', password: 'password2', role: 'user' } // 用户2
];

app.post('/api/login', (req, res) => {
  const { username, password } = req.body; // 获取请求体中的用户名和密码
  console.log(`Login attempt with username: ${username}, password: ${password}`); // 添加调试信息
  const user = users.find(u => u.username === username && u.password === password); // 查找用户
  if (user) {
    console.log('Login successful:', user); // 添加调试信息
    res.json({ user }); // 返回用户信息
  } else {
    console.log('Invalid credentials'); // 添加调试信息
    res.status(401).send('Invalid credentials'); // 返回 401 状态码
  }
});

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`); // 启动服务器
});
