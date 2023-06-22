from post.processing.tag_parts_of_speech import tag_parts_of_speech_ngrams


def lambda_handler(event, context):
    print(f"Event: {event}")
    print(f"Context: {context}")
    print(f"Categorizing text: {event['text']}")
    texts = event["text"]
    final_tags = tag_parts_of_speech_ngrams(texts)
    return final_tags
