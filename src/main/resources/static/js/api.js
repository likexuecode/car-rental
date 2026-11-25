/* 公共脚本：登录态、请求封装、导航、工具函数 */

const Auth = {
  get token() { return localStorage.getItem('cr_token'); },
  get user() {
    try { return JSON.parse(localStorage.getItem('cr_user') || 'null'); } catch (e) { return null; }
  },
  save(data) {
    localStorage.setItem('cr_token', data.token);
    localStorage.setItem('cr_user', JSON.stringify(data.user));
  },
  clear() {
    localStorage.removeItem('cr_token');
    localStorage.removeItem('cr_user');
  },
  requireLogin() {
    if (!this.token) {
      location.href = 'login.html';
      return false;
    }
    return true;
  }
};

async function request(url, options = {}) {
  const opt = { method: options.method || 'GET', headers: {} };
  if (Auth.token) opt.headers['Authorization'] = Auth.token;
  if (options.body !== undefined) {
    if (options.form) {
      opt.body = options.body;
    } else {
      opt.headers['Content-Type'] = 'application/json';
      opt.body = JSON.stringify(options.body);
    }
  }
  let res;
  try {
    res = await fetch(url, opt);
  } catch (e) {
    throw new Error('后端服务未部署，请稍后重试或联系管理员');
  }
  if (!res.ok) {
    throw new Error('后端服务暂不可用（' + res.status + '），请稍后重试');
  }
  let data;
  try {
    data = await res.json();
  } catch (e) {
    throw new Error('后端返回异常响应，请稍后重试');
  }
  if (data.code === 401) {
    Auth.clear();
    if (!location.pathname.endsWith('login.html')) location.href = 'login.html';
    throw new Error(data.msg || '请先登录');
  }
  if (data.code !== 0) throw new Error(data.msg || '操作失败');
  return data.data;
}

const api = {
  get: (url) => request(url),
  post: (url, body) => request(url, { method: 'POST', body: body || {} }),
  put: (url, body) => request(url, { method: 'PUT', body: body || {} }),
  del: (url) => request(url, { method: 'DELETE' }),
  upload: (file) => {
    const form = new FormData();
    form.append('file', file);
    return request('/api/upload', { method: 'POST', body: form, form: true });
  }
};

/* ==================== 导航 ==================== */
function renderNav(active) {
  const box = document.getElementById('nav');
  if (!box) return;
  const user = Auth.user;
  const items = [
    ['index.html', '首页'],
    ['orders.html', '我的订单'],
    ['service.html', '客服与公告'],
    ['profile.html', '个人中心']
  ];
  if (user && user.role === 'ADMIN') items.push(['admin.html', '管理后台']);
  const links = items.map(([href, text]) =>
    `<a href="${href}" class="${href === active ? 'active' : ''}">${text}</a>`).join('');
  box.innerHTML = `<div class="nav-inner">
    <a class="logo" href="index.html">汽车租赁管理系统</a>
    <div class="links">${links}</div>
    <div class="user">
      ${user ? `<span>${escapeHtml(user.name || user.account)}</span><a href="javascript:logout()">退出</a>`
             : `<a href="login.html">登录 / 注册</a>`}
    </div>
  </div>`;
}

async function logout() {
  try { await api.post('/api/auth/logout', {}); } catch (e) { /* 忽略 */ }
  Auth.clear();
  location.href = 'login.html';
}

/* ==================== 工具 ==================== */
function escapeHtml(str) {
  if (str === null || str === undefined) return '';
  return String(str).replace(/[&<>"']/g, (c) => ({
    '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
  })[c]);
}

let toastTimer = null;
function toast(msg, isError) {
  let box = document.getElementById('toast');
  if (!box) {
    box = document.createElement('div');
    box.id = 'toast';
    document.body.appendChild(box);
  }
  box.textContent = msg;
  box.className = 'show' + (isError ? ' err' : '');
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => { box.className = ''; }, 2600);
}

function fail(e) {
  toast(e && e.message ? e.message : '操作失败', true);
}

function money(value) {
  return '¥' + Number(value || 0).toLocaleString('zh-CN');
}

function fmt(value) {
  if (!value) return '-';
  return String(value).replace('T', ' ').slice(0, 16);
}

function statusTag(status) {
  const map = {
    '待支付': 'orange', '已支付': 'green', '已取车': 'blue',
    '归还审核中': 'orange', '已完成': 'green',
    '待审核': 'orange', '通过': 'green', '不通过': 'red',
    '待处理': 'orange', '已回复': 'green',
    '未支付': 'orange', '可租': 'green'
  };
  return `<span class="tag ${map[status] || ''}">${escapeHtml(status || '-')}</span>`;
}

function openModal(id) { document.getElementById(id).classList.add('show'); }
function closeModal(id) { document.getElementById(id).classList.remove('show'); }

function nowLocal() {
  const d = new Date();
  const pad = (n) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
}
