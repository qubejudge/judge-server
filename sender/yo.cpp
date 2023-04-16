#include<bits/stdc++.h>
using namespace std;


#define ll long long
const int N = 1e5 + 1;
const int MOD = 998244353;


void solve() {
     int n;
     cin >> n;
     vector<vector<int > > a(n);
     for(int i = 0; i < n; i++) {
          vector<int> v;
          int sz;
          cin >> sz;
          for(int j = 0; j < sz; j++) {
               int x;
               cin >> x;
               v.push_back(x);
          }
          a[i] = v;
     }
     int ans = 0;
     vector<int> sum;
     for(auto v : a) {
          int sz = v.size();
          int left = 0;
          for(int i = 1; i < sz; i++) {
               if(v[i] < v[i - 1]) {
                    sum.push_back(i - left);
                    ans += min(i - left, sz  - 1 - i);
                    // cout << i - left << " ";
                    left = i;
               }
          }
          sum.push_back(sz - left);
     }
          // cout << sz - left << " ";
        //   if(sum.size() == 1) {
        //        ans += sz;
        //        continue;
        //   }
        //   sort(sum.begin(), sum.end());
        //   int tot = sum[0];
          for(int i = 0; i < sum.size(); i++) {
               ans += sum[i];
               // cout << sum[i] << " ";
          }
        //   for(int i = 1; i < sum.size(); i++) {
        //        tot += sum[i];
        //        ans += tot;
        //   }
        //   cout << '\n';

     cout << ans << '\n';
          }




int main() {
     cin.sync_with_stdio(0);
     cin.tie(0);
     int tc = 1;
     cin >> tc;
     for(int tt = 0; tt < tc; tt++) {
          solve();
     }
     return 0;
}