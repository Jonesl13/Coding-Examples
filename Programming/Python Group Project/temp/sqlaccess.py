import sqlite3

conn = sqlite3.connect('Database.db')

c = conn.cursor()

def createTables(c):
    c.execute('''CREATE TABLE USERS ([uid] INTEGER PRIMARY KEY,
				 [username] VARCHAR(30) NOT NULL UNIQUE,
				 [password] VARBINARY(100) NOT NULL,
				 [role] VARCHAR(20))
			  ''')
    conn.commit()
			  
def populateTables(c):
    c.execute('''INSERT INTO USERS VALUES (1, 'threefish', 01010101, 'fish')''')
    conn.commit()

createTables(c)
c.execute("SELECT * FROM USERS")
print(c.fetchall())
conn.close()
