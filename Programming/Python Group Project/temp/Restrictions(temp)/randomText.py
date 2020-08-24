import random
import string

def randomTextGenerator():
    length = random.randint(15,30)
    Text = ''
    for x in range(length):
        choice = random.randint(0,1)
        if choice>0.5:
            t = random.choice(string.ascii_letters)
            Text = Text + t
        else:
            t = str(random.randint(0,9))
            Text = Text+t
    return Text
        