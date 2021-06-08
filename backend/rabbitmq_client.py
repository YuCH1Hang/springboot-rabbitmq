#!/usr/bin/env python
import pika
import json
import os
import shutil
connection = pika.BlockingConnection(
            pika.ConnectionParameters(host='localhost'))
channel = connection.channel()

#channel.exchange_declare(exchange='upload', exchange_type='fanout')

#result = channel.queue_declare(queue='first-queue', exclusive=True)
#queue_name = result.method.queue

#channel.queue_bind(exchange='upload', queue=queue_name)
queue_name = "first-queue"

print(' [*] Waiting for logs. To exit press CTRL+C')
src="dog-care_general-dog-care_main-image.jpg"
def callback(ch, method, properties, body):
        result = json.loads(body)
        print(" [x] %r" % body)
        print(result["timestamp"])
        dst=os.path.join("/home/hilbertw",result["username"],result["timestamp"]+".jpg")
        shutil.copyfile(src, dst)

channel.basic_consume( queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()
