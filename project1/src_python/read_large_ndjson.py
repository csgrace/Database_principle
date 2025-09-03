import json;
import time;
start_time = time.time()  # 程序开始时间
def read_large_ndjson(file_path,output_path):
   count = 0
   with (open(file_path, 'r', encoding='utf-8') as file, open(output_path, 'w', encoding='utf-8') as output_file):
       for line in file:
           if count >= 1000000:
               print(count)
               break
           json_obj = json.loads(line)
           formatted_json = json.dumps(json_obj)
           output_file.write(formatted_json)
           count += 1
           # if count >= 100:
           #     break
           #
           #
           #  json_obj = json.loads(line)
           #     # Format the JSON object with indentation
           #     # formatted_json = json.dumps(json_obj, indent=2)
           #     # Write the formatted JSON to the output file
           #     # output_file.write(formatted_json + '\n')
           #  print(json_obj)
           #  count += 1

read_large_ndjson("D:\\PyCharm 2024.2.4\\pubmed24n.ndjson",
                                 "D:\\PyCharm 2024.2.4\\WholePython10.txt")
print("insert successfully")
end_time = time.time()  # 程序结束时间

runtime = end_time - start_time  # 计算运行时间
print(f"程序运行时间：{runtime}秒")