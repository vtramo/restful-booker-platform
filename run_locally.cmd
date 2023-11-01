@echo off

echo:
echo ####### RESTFUL-BOOKER-PLATFORM #######
echo ####                               ####
echo ####    STARTING APPLICATION...    ####
echo ####                               ####
echo #######################################
echo:

for /f "delims=" %%a in ('dir auth\target\*.jar /B /O:D') do set "auth_jar=%%a"
START /B java -jar -Dspring.profiles.active=dev auth/target/%auth_jar% > auth.log

for /f "delims=" %%a in ('dir booking\target\*.jar /B /O:D') do set "booking_jar=%%a"
START /B java -jar -Dspring.profiles.active=dev booking/target/%booking_jar% > booking.log

for /f "delims=" %%a in ('dir room\target\*.jar /B /O:D') do set "room_jar=%%a"
START /B java -jar -Dspring.profiles.active=dev room/target/%room_jar% > room.log

for /f "delims=" %%a in ('dir report\target\*.jar /B /O:D') do set "report_jar=%%a"
START /B java -jar -Dspring.profiles.active=dev report/target/%report_jar% > report.log

for /f "delims=" %%a in ('dir branding\target\*.jar /B /O:D') do set "branding_jar=%%a"
START /B java -jar -Dspring.profiles.active=dev branding/target/%branding_jar% > branding.log

for /f "delims=" %%a in ('dir message\target\*.jar /B /O:D') do set "message_jar=%%a"
START /B java -jar -Dspring.profiles.active=dev message/target/%message_jar% > message.log &

for /f "delims=" %%a in ('dir assets\api\target\*.jar /B /O:D') do set "assets_jar=%%a"
START /B java -jar -Dspring.profiles.active=dev assets/api/target/%assets_jar% > ui.log

for /f "delims=" %%a in ('dir proxy\target\*.jar /B /O:D') do set "proxy_jar=%%a"
START /B java -jar proxy/target/%proxy_jar% > proxy.log

call node .utilities/monitor/local_monitor.js

echo:
echo ####### RESTFUL-BOOKER-PLATFORM #######
echo ####                               ####
echo ####      APPLICATION READY        ####
echo ####                               ####
echo ####         Available at:         ####
echo ####     http://localhost:8080     ####
echo ####                               ####
echo ####      PRESS ENTER TO QUIT      ####
echo ####                               ####
echo #######################################
echo:
set /p=

echo Exiting Restful-booker-platform....
taskkill /f /im java.exe
taskkill /f /im node.exe