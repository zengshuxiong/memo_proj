import os
root = ".."

for dirpath, dirnames, filenames in os.walk(root):
    print(dirpath)
    print("    sub_folders=", dirnames)
    for filepath in filenames:
        print(os.path.join(dirpath, filepath))