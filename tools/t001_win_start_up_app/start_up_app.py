#import os
import win32api
import time

#os.system('start \"D:\\Program Files\\Foxmail 7.2\\Foxmail.exe\"')

# 其参数含义如下所示。
# hwnd：父窗口的句柄，如果没有父窗口，则为0。
# op：要进行的操作，为“open”、“print”或者为空。
# file：要运行的程序，或者打开的脚本。
# params：要向程序传递的参数，如果打开的为文件，则为空。
# dir：程序初始化的目录。
# bShow：是否显示窗口。//ShowCmd

# ShowCmd 参数可选值:SW_HIDE = 0; {隐藏}
#  SW_SHOWNORMAL = 1; {用最近的大小和位置显示, 激活}
#  SW_NORMAL = 1; {同 SW_SHOWNORMAL}
#  SW_SHOWMINIMIZED = 2; {最小化, 激活}
#  SW_SHOWMAXIMIZED = 3; {最大化, 激活}
#  SW_MAXIMIZE = 3; {同 SW_SHOWMAXIMIZED}
#  SW_SHOWNOACTIVATE = 4; {用最近的大小和位置显示, 不激活}
#  SW_SHOW = 5; {同 SW_SHOWNORMAL}
#  SW_MINIMIZE = 6; {最小化, 不激活}
#  SW_SHOWMINNOACTIVE = 7; {同 SW_MINIMIZE}
#  SW_SHOWNA = 8; {同 SW_SHOWNOACTIVATE}
#  SW_RESTORE = 9; {同 SW_SHOWNORMAL}
#  SW_SHOWDEFAULT = 10; {同 SW_SHOWNORMAL}
#  SW_MAX = 10; {同 SW_SHOWNORMAL}

win32api.ShellExecute(0, 'open', '"D:\\Program Files\\Foxmail 7.2\\Foxmail.exe\"', '', '', 6)

win32api.ShellExecute(0, 'open', 'D:\\apps\\Notepad++\\notepad++.exe', '', '', 6)

# win32api.ShellExecute(0, 'open', '"D:\\Program Files (x86)\\SharpDevelop\\5.1\\bin\\SharpDevelop.exe"', '', '', 6)

# win32api.ShellExecute(0, 'open', '"D:\Program Files\Fiddler\Fiddler.exe"', '', '', 6)

win32api.ShellExecute(0, 'open', '"D:\Program Files (x86)\Tencent\WeChat\WeChat.exe"', '', '', 6)

win32api.ShellExecute(0, 'open', '"D:\\apps\\JetBrains\\IntelliJ_IDEA_2018.2.3\\bin\\idea64.exe"', '', '', 1)

win32api.ShellExecute(0, 'open', '"D:\\apps\\Kingsoft\\WPS_Office\\ksolaunch.exe"', '/prometheus /fromksolaunch /from=startmenu', '', 1)

print("start to sleep 60 secondes....")
time.sleep(60) # 休眠60秒
print("i waking back now~")

win32api.ShellExecute(0, 'open', '"D:\\ecplise\\eclipse-jee-mars-2-win32-x86_64\\eclipse\\eclipse.exe"', '', '', 1)

