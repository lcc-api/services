import json
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("input", type=str)
parser.add_argument("output", type=str)
args = parser.parse_args()

with open(args.input, 'r') as input_file:
    loaded: dict = json.load(input_file)

reformatted = {
    'llavaImageDescription': loaded['llava img description'],
    'imgTags': eval(loaded['img tag'])[0].split(' | '),
    'objects': {},
}


for keyy, valuee in loaded.items():
    if keyy in ['llava img description', 'img tag']:
        continue
    newObj = {
        'class': valuee['class'],
        'surroundingTags': eval(valuee['surrounding tags'])[0].split(' | '),
        'llavaDescription': valuee['llava description'],
    }
    reformatted['objects'][keyy] = newObj
    eval_fields = [
        ('centerLoc', 'center loc'),
        ('bboxSize', 'bbox size'),
        ('partAttributes', 'part attributes'),
    ]
    for newName, oldName in eval_fields:
        newObj[newName] = eval(valuee[oldName])


    for partialAttr in newObj['partAttributes'].values():
        partialAttr['bboxSize'] = eval(partialAttr['bbox size'])
        del partialAttr['bbox size']
        partialAttr['centerLoc'] = partialAttr['center loc']
        del partialAttr['center loc']


with open(args.output, 'w') as output_file:
    json.dump(reformatted, output_file, indent=2)
