import requests
from bs4 import BeautifulSoup
import pymysql

db_connection = pymysql.connect (
    host = 'localhost',
    user = '66day',
    password = 'habit1102!',
    database = 'habit66',
    charset='utf8mb4'
)

# 크롤링할 웹사이트 URL
url = 'https://insight-up.tistory.com/91'

response = requests.get(url)

if response.status_code == 200:
    soup = BeautifulSoup(response.text, 'html.parser')
    quotes = []

    # 'p' 태그를 찾아서 명언과 위인 이름 추출
    p_tags = soup.find_all('p')
    for p_tag in p_tags:

        wise_saying = p_tag.text
        great_person_tag = p_tag.find('b')

        if great_person_tag:  # b 태그가 있는 경우에만 추출
            great_person = great_person_tag.text

            # Wise saying와 Great person 값이 둘 다 공백이 아닌 경우에만 추가
            if wise_saying.strip() and great_person.strip():
                quotes.append({
                    'wise_saying': wise_saying.strip(),
                    'great_person': great_person.strip()
                })
    cursor = db_connection.cursor()

    for data in quotes:
        wise_saying = data['wise_saying']
        great_person = data['great_person']

        insert_query = "INSERT INTO Recommended_Quote (wise_saying, great_person) VALUES (%s, %s)"
        cursor.execute(insert_query, (wise_saying, great_person))

    db_connection.commit()

    cursor.close()
    db_connection.close()