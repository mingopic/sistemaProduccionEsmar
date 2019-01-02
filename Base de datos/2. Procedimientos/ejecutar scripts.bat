for %%G in (*.sql) do sqlcmd /S MINGO-LAPTOP\SQLEXPRESS /d esmarProd -E -i"%%G"
pause