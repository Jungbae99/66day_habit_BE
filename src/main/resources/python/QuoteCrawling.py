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
        br_tag = p_tag.find('br')

        if br_tag:  # br 태그가 있는 경우에만 추출
            wise_saying = br_tag.previous_sibling.strip()
            great_person_tag = br_tag.find_next('b')

            if great_person_tag:
                great_person = great_person_tag.text.strip()
            else:
                great_person = ""

            # Wise saying와 Great person 값이 둘 다 공백이 아닌 경우에만 추가
            if wise_saying and great_person:
                quotes.append({
                    'wise_saying': wise_saying,
                    'great_person': great_person
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