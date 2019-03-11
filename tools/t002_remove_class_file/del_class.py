import os

n = 0
for root, dirs, files in os.walk('..'):
    for name in files:
        if(name.endswith(".class")):
            n += 1
            print(n)
            print(os.path.join(root, name))
            os.remove(os.path.join(root,name))
