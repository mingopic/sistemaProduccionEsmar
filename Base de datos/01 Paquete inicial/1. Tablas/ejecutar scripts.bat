for %%G in (*.sql) do sqlcmd /S LAP-MINGO\SQLEXPRESS /d esmarProd -E -i"%%G"
pause