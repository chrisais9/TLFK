from __future__ import unicode_literals

from django.shortcuts import render, redirect
from django.http import JsonResponse

import json

def login(request):
    return JsonResponse({'status': 'Success'})


def LightInfo(request):
    x = request.GET['x']
    y = request.GET['y']
    CrossWalkId = request.GET['CrossWalkId']
    return JsonResponse([{'X': x}, {'Y': y}, {'CrossWalkId': CrossWalkId}], safe=False)
