def look_and_say(data, maxlen):

    iterations = []
    count = 0;
    appending = ""
    focus = ""

    for i in range(maxlen):

        if i == 0:
            focus = data
        else:
            focus = iterations[i - 1]

        length = len(focus)
        cur = ""
        prev = ""

        for x in range(length):
            cur = focus[x]

            if x == 0:
                count = 1
                
            if x > 0:
                prev = focus[x - 1]

                if cur == prev:
                    count += 1
                else:
                    appending = appending + str(count) + prev
                    count = 1

        appending = appending + str(count) + cur
        iterations.append(appending)
        appending = ""    

    return iterations


res1 = look_and_say("1", 10)
res2 = look_and_say("132", 8)

comp1 = ['11', '21', '1211', '111221', '312211', '13112221', '1113213211', '31131211131221', '13211311123113112211', '11131221133112132113212221']
comp2 = ['111312', '31131112', '1321133112', '11131221232112', '31131122111213122112', '13211321223112111311222112', '1113122113121122132112311321322112', '311311222113111221221113122112132113121113222112']
if res1 == comp1:
    print("Success1")
if res2 == comp2:
    print("Success2")