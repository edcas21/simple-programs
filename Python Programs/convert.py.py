import math

class InvalidInputException(Exception):
	pass
	
def tempConvert(temp, unit):
	
	f = "fF"
	c = "cC"
	res = 0
	
	#convert to Celcius
	if unit in f:
		res = round((temp - 32) * (5/9))
		print(res, "C")
	#convert to Farenheit
	if unit in c:
		res = round((temp * (9/5)) + 32)
		print(res, "F")
	
	
#Function to validate user input
def validInput(string):

	temp = 0
	u = ""
	digits = ""
	sep = []
	
	#Make sure string has separator
	if '*' not in string:
		return (False, u, temp)
	else:
		sep = string.split('*')
	
	#Validate left of separator
	
	#Check for neg num indicator
	if sep[0][0] == '-':
		digits = sep[0][1:]
	else:
		digits = string[0][0]
		
		#Check to see if chars before separator are numbers
	if digits.isdigit() == False:
		return(False, u, temp)
	else:
		temp = int(sep[0])
		
	#Validate right of separator
	
	#Make sure there's only one character
	if len(sep[1]) > 1:
		return (False, u, temp)
		
	u = sep[1]
	
	units = "fcFC"
	
	#Check unit specifier
	if u not in units:
		return(False, u, temp)
	
	"""
	returns tuple containing:
	1. Whether it was succesful or not; True or False
	2. Unit specifier
	3. Temperature to convert
	"""
	return(True, u, temp)
	
#Main	

while 1:
	
	print("Enter -1 to quit.")
	enter = input("Enter temperature to convert: follow format. ex:  12*F,  -1*C\n")
	
	if enter == "-1":
		break
	
	tup = validInput(enter)
	
	while tup[0] == False:
		enter = input("Invalid format. Try again: ex: 12*F, -1*C\n")
		tup = validInput(enter)
	
	tempConvert(tup[2], tup[1])
	print()
	
