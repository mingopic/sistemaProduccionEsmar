for %%G in (*.sql) do sqlcmd /S MINGO-LAP\SQLEXPRESS /d esmarProd -E -i"%%G"
pause