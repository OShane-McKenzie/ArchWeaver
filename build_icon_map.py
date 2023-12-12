import os
import json

def gen_icon_url(folder_path, base_url):
    data = {}

    for filename in os.listdir(folder_path):
        if filename.endswith(('.jpg', '.jpeg', '.png', '.gif')):
            image_name, extension = os.path.splitext(filename)
            image_url = f"{base_url}/{image_name}{extension}"
            data[f"{image_name}"] = image_url

    json_file_path = os.path.join(folder_path, 'icons.json')
    with open(json_file_path, 'w') as json_file:
        json.dump(data, json_file, indent=4)

folder_path = '/home/lite/Workspace/Kotlin/ArchWeaver/icons'
base_url = 'https://oshane-mckenzie.github.io/ArchWeaver'
gen_icon_url(folder_path, base_url)