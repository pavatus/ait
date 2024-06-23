a = '''
    "face=ceiling,facing=east,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "x": 180,
      "y": 270
    },
    "face=ceiling,facing=east,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "x": 180,
      "y": 270
    },
    "face=ceiling,facing=north,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "x": 180,
      "y": 180
    },
    "face=ceiling,facing=north,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "x": 180,
      "y": 180
    },
    "face=ceiling,facing=south,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "x": 180
    },
    "face=ceiling,facing=south,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "x": 180
    },
    "face=ceiling,facing=west,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "x": 180,
      "y": 90
    },
    "face=ceiling,facing=west,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "x": 180,
      "y": 90
    },
    "face=floor,facing=east,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "y": 90
    },
    "face=floor,facing=east,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "y": 90
    },
    "face=floor,facing=north,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off"
    },
    "face=floor,facing=north,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on"
    },
    "face=floor,facing=south,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "y": 180
    },
    "face=floor,facing=south,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "y": 180
    },
    "face=floor,facing=west,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "y": 270
    },
    "face=floor,facing=west,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "y": 270
    },
    "face=wall,facing=east,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "x": 90,
      "y": 90
    },
    "face=wall,facing=east,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "x": 90,
      "y": 90
    },
    "face=wall,facing=north,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "x": 90
    },
    "face=wall,facing=north,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "x": 90
    },
    "face=wall,facing=south,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "x": 90,
      "y": 180
    },
    "face=wall,facing=south,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "x": 90,
      "y": 180
    },
    "face=wall,facing=west,type=$A,powered=false": {
      "model": "ait:block/detector/$B_off",
      "x": 90,
      "y": 270
    },
    "face=wall,facing=west,type=$A,powered=true": {
      "model": "ait:block/detector/$B_on",
      "x": 90,
      "y": 270
    },
'''

variants = [ 'flight', 'power', 'crashed', 'door_locked', 'door_open', 'sonic', 'alarms' ]
models = [ 'flight', 'power', 'crashed', 'locked', 'open', 'sonic', 'alarms' ]

buf = '''{
  "variants": {'''
for i in range(len(variants)):
    variant = variants[i]
    model = models[i]

    buf += a.replace('$A', variant).replace('$B', model)

buf += '''
  }
}
'''

print(buf)