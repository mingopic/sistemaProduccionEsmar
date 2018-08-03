for %%G in (*.sql) do sqlcmd /S EQUIPO-PC\SQLEXPRESS /d esmarProd -E -i"%%G"
pause