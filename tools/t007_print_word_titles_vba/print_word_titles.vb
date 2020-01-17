'打印word各个章节的标题
Sub PrintTitles() ''本段过程用于取得文中的题目（赋值给ar）与内容（赋值给br）
    On Error Resume Next
    x = 0
    'y = 0
    Dim ar
    
    title_level = 0
    preNumCode = ""
    
    Dim para
    Open "c:\tmp\titles.txt" For Output As 1
    Dim text As String

    For Each y1 In ActiveDocument.Paragraphs
        x = x + 1
        t = ActiveDocument.Paragraphs(x).Style
        
        '-------------------------------
        '"标题 1" "标题 2" , ...
        '-------------------------------
        If Left(t, 3) = "标题 " Then
            title_level = CInt(Mid(t, 4, 5))
            
            Set para = ActiveDocument.Paragraphs(x)
            Set ar = ActiveDocument.Paragraphs(x).Range '''''''取得题目
            preNumCode = ar.ListFormat.ListString
            
            'y = y + 1 ''本变量用于确定是否循环到了下一个标题开始的地方。
            
            'Debug.Print Strings.Space(title_level * 4 - 3) & " " & preNumCode & " " & ar
            text = Strings.Space(title_level * 4 - 3) & " " & preNumCode & " " & ar
            Print #1, text
        End If
    Next
    
    Close #1
    MsgBox "finished."
End Sub


