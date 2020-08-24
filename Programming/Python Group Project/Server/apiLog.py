import logging
import time
import threading
from api import app, sendMail
from flask import Flask, request, current_app

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

def sendAlert(messageLog):
    sendMail('ALERT', 'chester.huang99@gmail.com', 'Notice: '+messageLog)

def sendBackupLog():
	app_context = app.app_context()
	app_context.push()

	while True:
		logger.info("Sent backup log to admin")
		sendMail('Remote Backup Log', 'macblowfish@gmail.com', 'Please find the backup log attached', attachment=True)
		time.sleep(300)

def startBackup():
	backup = threading.Thread(target=sendBackupLog, name='logbackup')
	backup.start()

app_context = app.app_context()
app_context.push()

logging.basicConfig(level=logging.INFO, filename='log.log', filemode='a', format='%(name)s - %(asctime)s - %(levelname)s - %(message)s', datefmt='%d-%b-%y %H:%M:%S')
logger = logging.getLogger('SecLog')