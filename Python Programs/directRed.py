def dirReduc(directions):
    directions = [dir.lower() for dir in directions]
    unecessary = {"east":"west", "south":"north", "west":"east", "north":"south"}
    reduced = []
    temp = [reduced.pop() if reduced and unecessary[dir] == reduced[-1] else reduced.append(dir) for dir in directions]
    del temp
    return reduced

print(dirReduc(["NORTH", "SOUTH", "SOUTH", "EAST", "WEST", "NORTH", "WEST"]))