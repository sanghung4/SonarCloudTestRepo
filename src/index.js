const { eclipseLoad } = require('./eclipse_load_count');
const { mincronLoad } = require('./mincron_load_count');

const { logger } = require('./logger');

async function main() {
  let scriptToRun = process.env.ERP;

  if (scriptToRun === 'ECLIPSE') {
    await eclipseLoad();
  }

  if (scriptToRun === 'MINCRON') {
    await mincronLoad();
  }
}

main().catch((err) => {
  if (err.isAxiosError) {
    logger.error(err.toJSON());
  } else {
    logger.error(err);
  }
  process.exit(1);
});
