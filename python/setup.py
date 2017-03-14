from setuptools import setup, find_packages

setup(
    name='magritte',
    version='0.1',
    description='Magritte - Model Training',
    url="",
    author='Yoann Benoit',
    author_email='',
    license='new BSD',
    packages=find_packages(),
    install_requires=['tensorflow'],
    tests_require=[],
    scripts=[],
    py_modules=["magritte_training"],
    include_package_data=True,
    zip_safe=False
)
