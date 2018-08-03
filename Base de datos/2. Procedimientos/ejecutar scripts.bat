for %%G in (*.sql) do sqlcmd /S MINGO-LAP /d esmarProd -E -i"%%G"
pause