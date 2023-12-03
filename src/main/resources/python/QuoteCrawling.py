import requests
from bs4 import BeautifulSoup
import pymysql
import yaml

# application.yml 파일의 경로 지정
yml_path = '/home/ubuntu/app/src/main/resources/application.yml'

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

    db_connection = pymysql.connect(**db_config)
    try:
        cursor = db_connection.cursor()

        for data in quotes:
            wise_saying = data['wise_saying']
            great_person = data['great_person']

            insert_query = "INSERT INTO Recommended_Quote (wise_saying, great_person) VALUES (%s, %s)"
            cursor.execute(insert_query, (wise_saying, great_person))

        db_connection.commit()
    finally:
        cursor.close()
        db_connection.close()