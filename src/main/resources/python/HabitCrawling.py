import requests
from bs4 import BeautifulSoup
import pymysql
import re
import yaml

# application.yml 파일의 경로 지정
yml_path = 'src/main/resources/application.yml'

# application.yml 파일 읽어오기
with open(yml_path, 'r') as ymlfile:
    cfg = yaml.safe_load(ymlfile)

# 데이터베이스 연결 설정
db_config = {
    'host': cfg['spring']['datasource']['url'].split('//')[1].split(':')[0],
    'user': cfg['spring']['datasource']['username'],
    'password': cfg['spring']['datasource']['password'],
    'database': cfg['spring']['datasource']['url'].split('/')[-1].split('?')[0],
    'charset': 'utf8mb4'
}

def trim_habit_subject(subject):
    return subject.strip()

url = 'https://www.betterup.com/blog/good-habits'

response = requests.get(url)

if response.status_code == 200:
    # HTML 파싱
    soup = BeautifulSoup(response.text, 'html.parser')

    recommended_habit = []

    for h3 in soup.find_all('h3'):
        habit_subject = h3.text.strip()

        cleaned_subject = re.sub(r'[\d.]+', '', habit_subject)
        cleaned_subject = trim_habit_subject(cleaned_subject)

        ol = h3.find_next('ol')
        if ol:
            habit_names = [li.text.strip() for li in ol.find_all('li')]

            # 'habit_subject'와 'habit_name'을 함께 저장
            for habit_name in habit_names:
                recommended_habit.append({
                    'habit_subject': cleaned_subject,
                    'habit_name': habit_name
                })

    db_connection = pymysql.connect(**db_config)
    try:
        cursor = db_connection.cursor()

        for data in recommended_habit:
            habit_name = data['habit_name']

            insert_query = "INSERT INTO recommended_habit (habit_subject, habit_name) VALUES (%s, %s)"
            cursor.execute(insert_query, (data['habit_subject'], habit_name))

        db_connection.commit()

    finally:
        cursor.close()
        db_connection.close()