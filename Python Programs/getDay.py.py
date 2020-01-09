import datetime

def getDay(date):
	month, day, year = (int(x) for x in date.split('/'))
	res = datetime.date(year, month, day)
	print(res.strftime("%A"))

class DateFormatError(ValueError):
	pass
	
def validDate(date):
	try:
		getDay(date)
	except:
		print("Incorrect date format. Should be MM/DD/YYYY")
		
#Main

while 1:
	print("Enter -1 to quit.")
	date = input("Enter date (M/DD/YYYY): ")
	if date == "-1":
		break
	validDate(date)
	print()
	
	
	