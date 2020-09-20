@echo off

set dllpath=E:\Project\10_study\jvm_projs\jvmti_demo\x64\Debug

set path=%dllpath%;%path%

java -agentpath:"%dllpath%\jvmti_evt_ex.dll" -cp .  App

pause