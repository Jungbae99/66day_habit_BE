import requests
from bs4 import BeautifulSoup
import pymysql

db_connection = pymysql.connect (
    host = 'localhost',
    user = '66day',
    password = 'habit1102!',
    database = 'habit66',
    charset='utf8mb4'
#     host = 'localhost',
#     user = 'sa',
#     password = '999',
#     database = '66day',
#     charset='utf8mb4'
)

# 크롤링할 웹사이트 URL
url = 'https://www.betterup.com/blog/good-habits'

# 웹페이지 요청
response = requests.get(url)

# 웹페이지가 정상적으로 응답하는지 확인
if response.status_code == 200:
    # HTML 파싱
    soup = BeautifulSoup(response.text, 'html.parser')

    # '습관' 키워드를 포함한 텍스트 내용 찾기
    recommended_habit = []

    # 'h3' 요소를 찾아서 'celebrity'로 저장
    for h3 in soup.find_all('h3'):
        celebrity = h3.text.strip()

        # 'ol' 요소를 찾아서 'li'를 'habit_name'으로 저장
        ol = h3.find_next('ol')
        if ol:
            habit_names = [li.text.strip() for li in ol.find_all('li')]

            # 'celebrity'와 'habit_name'을 함께 저장
            for habit_name in habit_names:
                recommended_habit.append({
                    'celebrity': celebrity,
                    'habit_name': habit_name
                })

    # 데이터베이스 연결 및 커서 생성
    cursor = db_connection.cursor()

    # 'recommended_habit' 리스트에 저장된 데이터를 데이터베이스에 삽입
    for data in recommended_habit:
        habit_name = data['habit_name']

        # 데이터베이스에 데이터 삽입
        insert_query = "INSERT INTO recommended_habit (celebrity, habit_name) VALUES (%s, %s)"
        cursor.execute(insert_query, (data['celebrity'], habit_name))

    db_connection.commit()

    # 커서와 데이터베이스 연결 닫기
    cursor.close()
    db_connection.close()
