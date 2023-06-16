const { v4: uuidv4 } = require('uuid');

const pool = [];

function getSessionId() {
  if (pool.length > 0) {
    return pool.pop();
  }

  return uuidv4();
}

function returnSessionId(sessionId) {
  pool.push(sessionId);
}

module.exports = {
  getSessionId,
  returnSessionId,
};
