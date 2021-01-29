git add .
set /p commitmsg=Enter Commit Message: 
git commit -am "%commitmsg%"
git push origin main
pause