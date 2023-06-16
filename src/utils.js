function formatElapsedTime(elapsedTime) {
  const totalSeconds = elapsedTime / 1000;
  const minutes = totalSeconds / 60;
  const seconds = `${totalSeconds % 60}`.padStart(2, 0);
  return `${minutes}:${seconds}`;
}

exports.formatElapsedTime = formatElapsedTime;
