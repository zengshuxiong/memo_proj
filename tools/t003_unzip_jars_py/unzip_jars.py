# -*- coding: utf-8 -*-
import os
import shutil
import zipfile

root = "D:/eden/path1/pro1/lib"

count = 1
# def getSumDir():
#     sumfilelist = os.listdir(os.getcwd())
#     for dir in sumfilelist:
#         if ".idea" not in dir:
#             classify(dir)
#
#
def getlibDir():
    sumfilelist = os.listdir(root)
    for dir in sumfilelist:
        print(dir);
        if dir.endswith(".jar"):
            jieyajar(dir)
#
 
def jieyajar(jarname):
    jarfile = root + "/" + jarname
    newpath = root + "/tmp/" + jarname
    zfile = zipfile.ZipFile(jarfile, 'r')
    if not os.path.exists(newpath):
        os.makedirs(newpath)

    for file in zfile.namelist():
        zfile.extract(file, newpath)

    # for file in zfile.namelist():
    #     if os.path.isfile(file):
    #         if "MANIFEST.MF" in file:
    #             zfile.extract("MANIFEST.MF", newpath)
    #     else:
    #         list = os.listdir(file)
    #         for dir in list:
    #             if "MANIFEST.MF" in file:
    #                 zfile.extract("MANIFEST.MF", newpath)

    zfile.close

# def classify(path):
#     global count
#     if os.path.isfile(path):
#         if "MANIFEST.MF" in path:
#             print("file=" + dir)
#     else:
#         if 'META-INF' in path:
#             list = os.listdir(path)
#             for dir in list:
#                 print(dir)
 
 
# getSumDir()
getlibDir()