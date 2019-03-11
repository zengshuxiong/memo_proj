import os
root = "D:\\eden\\path1\\pro1\\lib\\tmp"

metaFolderName = "META-INF"
metaFileName = "MANIFEST.MF"

def readAndCollectMetaFiles():
    for filenames in os.listdir(root):
        print(filenames)
        # for jarFolder in os.listdir(root + "/" + filenames):
        mfile = root + "/" + filenames + "/META-INF/MANIFEST.MF"
        print(mfile)
        if os.path.exists(mfile):
            readMetaFile(mfile)

def readMetaFile(metaFileName):
    outf = open(root + '/all-meta.txt', 'a')
    f = open(metaFileName)
    lines = f.readlines()

    outf.writelines("\n\n" + metaFileName + "\n")
    outf.writelines(lines)

readAndCollectMetaFiles()