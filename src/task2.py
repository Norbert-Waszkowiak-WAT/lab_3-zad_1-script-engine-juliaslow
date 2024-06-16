import requests
import random
import string

BASE_URL = 'http://localhost:8080/api'

def generate_random_string(length):
    letters = string.ascii_letters
    return ''.join(random.choice(letters) for _ in range(length))

def create_random_affiliation():
    url = BASE_URL + '/affiliations'
    data = {
        "name": generate_random_string(6),
        "parent": create_random_affiliation() if random.random() > 0.5 else None  # Optional parent affiliation
    }
    response = requests.post(url, json=data)
    if response.status_code == 200:
        print("Affiliation created successfully")
        print("Response JSON:", response.json())
    else:
        print("Failed to create affiliation")
        print("Status code:", response.status_code)
        print("Response text:", response.text)

def create_random_author():
    url = BASE_URL + '/authors'
    affiliation = create_random_affiliation()
    if affiliation is None:
        return
    data = {
        "name": generate_random_string(6),
        "surname": generate_random_string(6),
        "affiliation": affiliation
    }
    response = requests.post(url, json=data)
    if response.status_code == 200:
        print("Author created successfully")
        print("Response JSON:", response.json())
    else:
        print("Failed to create author")
        print("Status code:", response.status_code)
        print("Response text:", response.text)

def create_random_publisher():
    url = BASE_URL + '/publishers'
    data = {
        "name": generate_random_string(6),
        "location": generate_random_string(6)
    }
    response = requests.post(url, json=data)
    if response.status_code == 200:
        print("Publisher created successfully")
        print("Response JSON:", response.json())
    else:
        print("Failed to create publisher")
        print("Status code:", response.status_code)
        print("Response text:", response.text)

def create_random_journal():
    url = BASE_URL + '/journals'
    data = {
        "baseScore": random.randint(1, 10),
        "title": generate_random_string(6),
        "publisher": create_random_publisher(),
        "issn": generate_random_string(4)
    }
    response = requests.post(url, json=data)
    if response.status_code == 200:
        print("Journal created successfully")
        print("Response JSON:", response.json())
    else:
        print("Failed to create journal")
        print("Status code:", response.status_code)
        print("Response text:", response.text)

def create_random_book():
    url = BASE_URL + '/books'
    data = {
        "isbn": generate_random_string(4),
        "year": random.randint(1950, 2023),
        "baseScore": random.randint(1, 10),
        "title": generate_random_string(15),
        "publisher": create_random_publisher(),
        "editor": create_random_author()
    }
    response = requests.post(url, json=data)
    if response.status_code == 200:
        print("Book created successfully")
        print("Response JSON:", response.json())
    else:
        print("Failed to create book")
        print("Status code:", response.status_code)
        print("Response text:", response.text)

def create_random_chapter():
    url = BASE_URL + '/chapters'
    data = {
        "score": random.randint(1, 100),
        "collection": generate_random_string(8),
        "title": generate_random_string(10),
        "authors": [create_random_author(), create_random_author()],
        "book": create_random_book()
    }
    response = requests.post(url, json=data)
    if response.status_code == 200:
        print("Chapter created successfully")
        print("Response JSON:", response.json())
    else:
        print("Failed to create chapter")
        print("Status code:", response.status_code)
        print("Response text:", response.text)

def create_random_article():
    url = BASE_URL + '/articles'
    data = {
        "title": generate_random_string(10),
        "collection": generate_random_string(5),
        "score": random.randint(1, 100),
        "vol": random.randint(1, 10),
        "no": random.randint(1, 10),
        "articleNo": random.randint(1, 10),
        "journal": create_random_journal(),
        "authors": [create_random_author(), create_random_author()]
    }
    response = requests.post(url, json=data)
    if response.status_code == 201:
        print("Article created successfully")
        print("Response JSON:", response.json())
    else:
        print("Failed to create article")
        print("Status code:", response.status_code)
        print("Response text:", response.text)

def add_data():
    for _ in range(14):
        create_random_affiliation()
    for _ in range(15):
        create_random_author()
    for _ in range(14):
        create_random_publisher()
    for _ in range(14):
        create_random_journal()
    for _ in range(14):
        create_random_book()
    for _ in range(14):
        create_random_chapter()
    for _ in range(15):
        create_random_article()

if __name__ == "__main__":
    add_data()
