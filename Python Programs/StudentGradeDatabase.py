import random
import csv
import datetime
from os import path
import matplotlib.pyplot as plt
from operator import itemgetter
from collections import OrderedDict

"""
Notes:
Check to make sure that the phone number provided is only 10
digits long including the area code and accepts characters
which then need to be converted to digits
"""
# Reads students from a csv file
def readStudents(filename, studentDict, gradeStats, lengths):

    with open(filename, "r") as gradeFile:
        csv_reader = csv.reader(gradeFile, delimiter=",")
        for row in csv_reader:
            """Each field in the record is going to contain
            everything but the grades"""
            studentDict[row[0]] = {}
            studentDict[row[0]]["birthdate"] = validBirthdate(row[1])
            studentDict[row[0]]["address"] = row[2]
            phone = validPhone(row[3])
            studentDict[row[0]]["phone"] = phone
            grades = generateGrades(lengths)
            studentDict[row[0]]["grades"] = grades
            gradeStats[row[0]] = statistics(grades)


def writeStudents(studentDict, filename):
    with open(filename, mode="w") as csv_file:
        csv_writer = csv.writer(csv_file)

        for student in studentDict:
            row = [
                student,
                studentDict[student]["birthdate"],
                studentDict[student]["address"],
                studentDict[student]["phone"],
            ]
            csv_writer.writerow(row)
    print("Students info written to", filename, "sucessfully\n")


def generateLengths():
    # item list lengths
    hwLen = random.randint(3, 6)
    qLen = random.randint(3, 6)
    mLen = random.randint(2, 3)
    fLen = 2

    return [hwLen, qLen, mLen, fLen]


# ------------------------------------
# generate list of lists containing the grades
def generateGrades(lengths):

    # list of lists
    grades = [["Homework"], ["Quiz"], ["Midterm"], ["Final"]]

    # Generate the items for each of the lists within grades
    for list in grades:
        if list[0] == "Homework":
            for grade in range(lengths[0]):
                list.append(random.randint(0, 100))
        if list[0] == "Quiz":
            for grade in range(lengths[1]):
                list.append(random.randint(0, 100))
        if list[0] == "Midterm":
            for grade in range(lengths[2]):
                list.append(random.randint(0, 100))
        if list[0] == "Final":
            for grade in range(lengths[3]):
                list.append(random.randint(0, 100))

    return grades


# ------------------------------------
# Validate the birthdate
def validBirthdate(birthdate):
    valid = 0
    while valid == 0:
        try:
            datetime.datetime.strptime(birthdate, "%m/%d/%Y")
            valid = 1
        except ValueError:
            birthdate = str(input("Incorrect date format. Please enter: MM/DD/YYYY: "))
    return birthdate


# ------------------------------------
# phone validation
def validPhone(phone):
    # Make sure that the length of the phone number is 10 digits
    while len(phone) < 10 or len(phone) > 10:
        print("Phone number must be 10 digits long. Try again.")
        phone = input("Enter the student's phone number: ")

    # Convert characters into the corresponding digits
    phone = phone.replace("A", "2").replace("B", "2").replace("C", "2")
    phone = phone.replace("D", "3").replace("E", "3").replace("F", "3")
    phone = phone.replace("G", "4").replace("H", "4").replace("I", "4")
    phone = phone.replace("J", "5").replace("K", "5").replace("L", "5")
    phone = phone.replace("M", "6").replace("N", "6").replace("O", "6")
    phone = (
        phone.replace("P", "7").replace("Q", "7").replace("R", "7").replace("S", "7")
    )
    phone = phone.replace("T", "8").replace("U", "8").replace("V", "8")
    phone = (
        phone.replace("W", "9").replace("X", "9").replace("Y", "9").replace("Z", "9")
    )

    phone = phone.replace("a", "2").replace("b", "2").replace("c", "2")
    phone = phone.replace("d", "3").replace("e", "3").replace("f", "3")
    phone = phone.replace("g", "4").replace("h", "4").replace("i", "4")
    phone = phone.replace("j", "5").replace("k", "5").replace("l", "5")
    phone = phone.replace("m", "6").replace("n", "6").replace("o", "6")
    phone = (
        phone.replace("p", "7").replace("q", "7").replace("r", "7").replace("s", "7")
    )
    phone = phone.replace("t", "8").replace("u", "8").replace("v", "8")
    phone = (
        phone.replace("w", "9").replace("x", "9").replace("y", "9").replace("z", "9")
    )

    return phone


# ------------------------------------
def statistics(grades):
    # HW
    HWpoints = 0.0
    total_HW_points = 0.0
    # Quizzes
    Qpoints = 0.0
    total_Qpoints = 0.0
    # Midterms
    Mpoints = 0.0
    total_Mpoints = 0.0
    # Finals
    Fpoints = 0.0
    total_Fpoints = 0.0

    HW_scores = []
    Q_scores = []

    index = 0

    for category in grades:
        for grade in category:
            if index > 0:
                if isinstance(grade, str) == False:
                    if category[0] == "Homework":
                        total_HW_points += 100.0
                        HWpoints += grade
                        HW_scores.append(grade)
                    if category[0] == "Quiz":
                        Q_and_HW = grade
                        total_Qpoints += 100.0
                        Qpoints += grade
                        Q_scores.append(grade)
                    if category[0] == "Midterm":
                        total_Mpoints += 100.0
                        Mpoints += grade
                    if category[0] == "Final":
                        total_Fpoints += 100.0
                        Fpoints += grade
            index += 1
        index = 0

    calc_overall_grade = round(
        (
            ((HWpoints / total_HW_points) * 0.3)
            + ((Qpoints / total_Qpoints) * 0.1)
            + ((Mpoints / total_Mpoints) * 0.3)
            + ((Fpoints / total_Fpoints) * 0.3)
        )
        * 100
    )

    letter_grade = ""

    if calc_overall_grade <= 100 and calc_overall_grade >= 90:
        letter_grade = "A"
    elif calc_overall_grade <= 89 and calc_overall_grade >= 85:
        letter_grade = "BA"
    elif calc_overall_grade <= 84 and calc_overall_grade >= 80:
        letter_grade = "B"
    elif calc_overall_grade <= 79 and calc_overall_grade >= 75:
        letter_grade = "CB"
    elif calc_overall_grade <= 74 and calc_overall_grade >= 70:
        letter_grade = "C"
    elif calc_overall_grade <= 69 and calc_overall_grade >= 65:
        letter_grade = "DC"
    elif calc_overall_grade <= 64 and calc_overall_grade >= 60:
        letter_grade = "D"
    elif calc_overall_grade <= 59:
        letter_grade = "E"

    gradeStats = [calc_overall_grade, HW_scores, Q_scores, letter_grade]

    return gradeStats


# ------------------------------------
# adds a student to the dictionary of students
def addStudent(studentDict, gradeStats, lengths):
    studentInfo = {}

    name = input("\nEnter student name: ")

    birthdate = validBirthdate(str(input("Enter student birthdate (MM/DD/YYYY): ")))

    studentInfo["birthdate"] = birthdate

    address = input("Enter the student's address: ")
    studentInfo["address"] = address

    phone = validPhone(input("Enter the student's phone number: "))
    print()

    studentInfo["phone"] = phone

    grades = generateGrades(lengths)
    studentInfo["grades"] = grades
    gradeStats[name] = statistics(grades)

    studentDict[name] = studentInfo


def deleteStudent(studentDict, gradeStats):
    name = input("\nEnter name of student to remove: ")
    if name in studentDict:
        del studentDict[name]
        del gradeStats[name]
        print(name, "has been removed.\n")
    else:
        print("Student entered does not exist.\n")


# ------------------------------------
def printInfo(studentDict):

    choice = input("\nEnter all to print student info or student name for info: ")
    print()
    if choice == "all":

        Str = ""
        grades = []
        print()
        # Print the student info
        for student in studentDict:
            print("student:", student, end=" | ")
            info = studentDict[student]
            for field in info:
                if field != "grades" and field != "phone":
                    Str += field + ": " + info[field] + " | "
                elif field == "phone":
                    Str += "phone: " + info["phone"]

            print("{}".format(Str))
            grades = info["grades"]

            Str = ""
            index = 0
            # Print out the grades for each category
            print("Grades: ")
            for category in grades:
                for grade in category:
                    if isinstance(grade, str) == True:
                        Str += grade + ": "
                    elif index != (len(category) - 1):
                        Str += str(grade) + ", "
                    else:
                        Str += str(grade)
                    index += 1
                index = 0
                print("{}".format(Str))
                Str = ""
            print("*")
    else:

        info = {}
        Str = ""
        if choice in studentDict.keys():
            info = studentDict[choice]
            print("student:", choice, end=" | ")
            for field in info:
                if field != "grades" and field != "phone":
                    Str += field + ": " + info[field] + " | "
                elif field == "phone":
                    Str += "phone: " + info["phone"]

            print("{}".format(Str))

            grades = info["grades"]
            Str = ""
            index = 0  # get
            # Print out the grades for each category
            print("Grades: ")
            for category in grades:
                for grade in category:
                    if isinstance(grade, str) == True:
                        Str += grade + ": "
                    elif index != (len(category) - 1):
                        Str += str(grade) + ", "
                    else:
                        Str += str(grade)
                    index += 1
                index = 0
                print("{}".format(Str))
                Str = ""
            print("*")
        else:
            print("\n{ch} does not exist.\n".format(ch=choice))


def plotGrades(gradeStats):
    studentNum = range(1, len(gradeStats))
    grades = []

    # Fill in the grades array
    for student in gradeStats:
        grades.append(gradeStats[student][0])

    print(studentNum)
    print(grades)
    plt.plot(studentNum, grades)
    """plt.legend(shadow=True, loc="lower right")
    plt.ylim(0, 100)
    plt.title("Students Total Grades")
    plt.xlabel("Student Number")
    plt.ylabel("Gradees")"""

    # annotation
    """arrow_properties = {
        "facecolor": "black",
        "shrink": 0.1,
        "headlength": 10,
        "width": 2,
    }

    x = max(grades)
    y = grades.index(x)
    plt.annotate("Max Grade", xy=(x, y), arrowprops=arrow_properties)
    x = min(grades)
    y = grades.index(x)
    plt.annotate("Min Grade", xy=(x, y), arrowprops=arrow_properties)
    matplotlib.show()"""


# ------------------------------------
def show_statistics(gradeStats):
    HW_num = 0
    Q_num = 0

    for student in gradeStats:
        Q_num = len(gradeStats[student][2])

    for student in gradeStats:
        HW_num = len(gradeStats[student][1])

    HWinput = int(
        input(
            "\nThere are {num} homework assignments.\nChoose which homework to display statistics for. Enter 1-{num}: ".format(
                num=HW_num
            )
        )
    )
    print()
    while HWinput < 1 or HWinput > HW_num:
        print("Invalid selection!")
        HWinput = int(
            input(
                "There are {num} homework assignments.\nChoose which homework to display statistics for. Enter 1-{num}: ".format(
                    num=HW_num
                )
            )
        )
        print()

    Qinput = int(
        input(
            "There are {num} quizzes.\nChoose which quiz to display statistics for. Enter 1-{num}: ".format(
                num=Q_num
            )
        )
    )
    while Qinput < 1 or Qinput > Q_num:
        print("Invalid selection!")
        Qinput = int(
            input(
                "There are {num} quizzes.\nChoose which quiz to display statistics for. Enter 1-{num}: ".format(
                    num=Q_num
                )
            )
        )
        print()

    # Get the specific quiz scores
    spec_HWscores = {}
    for student in gradeStats:
        grade = gradeStats[student][1][(HWinput - 1)]
        spec_HWscores[student] = grade

    # Get the specific HW scores
    spec_Qscores = {}
    for student in gradeStats:
        grade = gradeStats[student][2][(Qinput - 1)]
        spec_Qscores[student] = grade

    # Get the max HW score
    maxHW_score = max(spec_HWscores, key=spec_HWscores.get)
    # Get the min HW score# get
    minHW_score = min(spec_HWscores, key=spec_HWscores.get)
    # Get the max Quiz score
    maxQ_score = max(spec_Qscores, key=spec_Qscores.get)
    # Get the min Quiz score
    minQ_score = min(spec_Qscores, key=spec_Qscores.get)

    print(
        "\nMax Homework score:\n{student} : Homework {HW} : grade: {grade}%".format(
            student=str(maxHW_score),
            HW=str(HWinput),
            grade=str(spec_HWscores[maxHW_score]),
        )
    )
    print(
        "Min Homework score:\n{student} : Homework {HW} : grade: {grade}%".format(
            student=str(minHW_score),
            HW=str(HWinput),
            grade=str(spec_HWscores[minHW_score]),
        )
    )

    print()

    print(
        "Max Quiz score:\n{student} : Quiz {Quiz} : grade: {grade}".format(
            student=str(maxQ_score),
            Quiz=str(Qinput),
            grade=str(spec_Qscores[maxQ_score]),
        )
    )
    print(
        "Min Quiz score:\n{student} : Quiz {Quiz} : grade: {grade}".format(
            student=str(minQ_score),
            Quiz=str(Qinput),
            grade=str(spec_Qscores[minQ_score]),
        )
    )
    print()

    """gradeStats = [
        calc_overall_grade, HW_scores, Q_scores, letter_grade
    ]"""

    # Print out sorted list of students based on overall grade
    grade_list = {}
    for student in gradeStats:
        student_grade = gradeStats[student][0]
        grade_list[student] = student_grade

    grade_list = OrderedDict(
        sorted(grade_list.items(), key=itemgetter(1), reverse=True)
    )

    for student in grade_list:
        letter_grade = gradeStats[student][3]
        print(
            "student: {}, overall grade: {}%, letter grade: {}".format(
                student, grade_list[student], letter_grade
            )
        )
    print()


def driver(studentDict, gradeStats, lengths):
    choice = input(
        "1. Save students' data on file\n2. Load students' data from file\n3.Add new student\n4. Remove student\n5. Display student information (either by one student or all of them)\n6. Display statistics about student's grades\n7. Exit\n\n"
    )

    res = len(studentDict) == 0
    if choice == "1":
        if res == False:
            filename = input("\nPlease enter name of file: ")
            writeStudents(studentDict, filename)
        else:
            print("\nThe database is empty. Please add a student.\n")
    elif choice == "2":
        filename = input("\nPlease enter name of file: ")
        exists = path.exists(filename)
        if exists == False:
            print("File entered does not exist.\n")
        else:
            readStudents(filename, studentDict, gradeStats, lengths)
            print("Students added succesfully\n")
    elif choice == "3":
        addStudent(studentDict, gradeStats, lengths)
    elif choice == "4":
        if res == False:
            deleteStudent(studentDict, gradeStats)
        else:
            print("\nThere are no students to delete. Please add a student.\n")
    elif choice == "5":
        if res == False:
            printInfo(studentDict)
        else:
            print("\nThe database is empty. Please add a student.\n")
    elif choice == "6":
        if res == False:
            show_statistics(gradeStats)
        else:
            print("\nThe database is empty. Please add a student.\n")
    elif choice == "7":
        return 8
    else:
        return 0

    return int(choice)


# ------------------------------------
gradeStats = {}
studentDict = {}
lengths = generateLengths()
ret = 0
while ret != 8:
    ret = driver(studentDict, gradeStats, lengths)
