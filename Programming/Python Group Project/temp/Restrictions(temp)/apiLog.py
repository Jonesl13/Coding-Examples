import logging
logging.basicConfig(level=logging.INFO, filename='log.log', filemode='a', format='%(name)s - %(asctime)s - %(levelname)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S')
logger = logging.getLogger('SecLog')

def logInfo(message):
	logger.info(message)

def logWarn(message):
	logger.warning(message)

def logError(message):
	logger.error(message)

def readLog():
	with open('log.log', 'r') as f:
		logs = f.read().splitlines()

	for i in logs:
		logger = i.split(' ')
		if logger[0] == 'SecLog':
			print(i)
