
import pybase64

def b64encode(value):
    return pybase64.standard_b64encode(str.encode(value)).decode('utf-8')

def b64decode(env, value):
    return pybase64.standard_b64decode(str.encode(value)).decode('utf-8')